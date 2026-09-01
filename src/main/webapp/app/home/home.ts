import { ChangeDetectionStrategy, Component, inject, OnInit, signal, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { HttpClient } from '@angular/common/http';
import { Chart, registerables } from 'chart.js';
import { QRCodeComponent } from 'angularx-qrcode';

// Register Chart.js components
Chart.register(...registerables);

interface DashboardStats {
  outgoingsByType: { category: string; count: number }[];
  incomingsByPerson: { category: string; count: number }[];
  incomingsWeeklyTrend: { weekLabel: string; count: number }[];
  currentMonthLabel: string;
}

@Component({
  selector: 'jhi-home',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [RouterLink, QRCodeComponent],
})
export default class Home implements OnInit, OnDestroy {
  // --- Public Properties & Signals ---
  public readonly account = inject(AccountService).account;
  networkAccessUrl = signal<string>('Loading...');
  currentMonthLabel = signal<string>('');
  currentYearLabel = signal<string>(new Date().getFullYear().toString());

  @ViewChild('outgoingsChart') outgoingsCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('trendChart') trendCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('personChart') personCanvas!: ElementRef<HTMLCanvasElement>;

  // --- Private Properties ---
  private readonly router = inject(Router);
  private readonly http = inject(HttpClient);
  private outgoingsChart: Chart | null = null;
  private trendChart: Chart | null = null;
  private personChart: Chart | null = null;

  ngOnInit(): void {
    this.http.get('/api/system/access-url', { responseType: 'text' }).subscribe({
      next: url => this.networkAccessUrl.set(url),
      error: () => this.networkAccessUrl.set('http://localhost:8080'),
    });

    if (this.account()) {
      this.loadDashboardStats();
    }
  }

  ngOnDestroy(): void {
    this.outgoingsChart?.destroy();
    this.trendChart?.destroy();
    this.personChart?.destroy();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  private loadDashboardStats(): void {
    this.http.get<DashboardStats>('/api/dashboard/stats').subscribe({
      next: stats => {
        this.currentMonthLabel.set(stats.currentMonthLabel);
        this.renderOutgoingsChart(stats.outgoingsByType);
        this.renderTrendChart(stats.incomingsWeeklyTrend, stats.currentMonthLabel);
        this.renderPersonChart(stats.incomingsByPerson);
      },
      error: err => console.error('Failed to load dashboard stats', err),
    });
  }

  private renderOutgoingsChart(data: { category: string; count: number }[]): void {
    if (this.outgoingsChart) this.outgoingsChart.destroy();
    this.outgoingsChart = new Chart(this.outgoingsCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: data.map(x => x.category || 'Uncategorized'),
        datasets: [
          {
            label: 'Outgoing Documents',
            data: data.map(x => x.count),
            backgroundColor: '#4e73df',
            borderColor: '#4e73df',
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 1 },
          },
        },
        plugins: {
          legend: { display: false },
          title: {
            display: true,
            text: `Total Outgoings by Document Type (${this.currentYearLabel()})`,
            font: { size: 14, weight: 'bold' },
          },
        },
      },
    });
  }

  private renderTrendChart(data: { weekLabel: string; count: number }[], monthLabel: string): void {
    if (this.trendChart) this.trendChart.destroy();
    this.trendChart = new Chart(this.trendCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: data.map(x => x.weekLabel),
        datasets: [
          {
            label: 'Received Documents',
            data: data.map(x => x.count),
            backgroundColor: '#1cc88a',
            borderColor: '#1cc88a',
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 1 },
          },
        },
        plugins: {
          legend: { display: false },
          title: {
            display: true,
            text: `Monthly Incomings Trend (${monthLabel})`,
            font: { size: 14, weight: 'bold' },
          },
        },
      },
    });
  }

  private renderPersonChart(data: { category: string; count: number }[]): void {
    if (this.personChart) this.personChart.destroy();
    this.personChart = new Chart(this.personCanvas.nativeElement, {
      type: 'doughnut',
      data: {
        labels: data.map(x => x.category || 'Unassigned'),
        datasets: [
          {
            data: data.map(x => x.count),
            backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#f6c23e', '#e74a3b', '#858796'],
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { boxWidth: 12, font: { size: 11 } },
          },
        },
      },
    });
  }
}
