import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbCollapse } from '@ng-bootstrap/ng-bootstrap/collapse';
import { NgbDropdown, NgbDropdownMenu, NgbDropdownToggle } from '@ng-bootstrap/ng-bootstrap/dropdown';
import { environment } from 'environments/environment';

import { AccountService } from 'app/core/auth/account.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LoginService } from 'app/login/login.service';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import {
  faAsterisk,
  faBuilding,
  faClockRotateLeft,
  faCloud,
  faCogs,
  faDatabase,
  faFileLines,
  faFlag,
  faHome,
  faInbox,
  faList,
  faLock,
  faScaleBalanced,
  faSignInAlt,
  faSignOutAlt,
  faTasks,
  faThList,
  faUser,
  faUserPlus,
  faUsersCog,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-navbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
  imports: [
    RouterLink,
    RouterLinkActive,
    FontAwesomeModule,
    NgbCollapse,
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownToggle,
    HasAnyAuthorityDirective,
  ],
})
export default class Navbar implements OnInit {
  readonly inProduction = signal(true);
  readonly isNavbarCollapsed = signal(true);
  readonly openAPIEnabled = signal(false);
  readonly version: string;
  readonly account = inject(AccountService).account;
  readonly faHome = faHome;
  readonly faInbox = faInbox;
  readonly faFileLines = faFileLines;
  readonly faUser = faUser;
  readonly faTasks = faTasks;
  readonly faThList = faThList;
  readonly faBuilding = faBuilding;
  readonly faClockRotateLeft = faClockRotateLeft;
  readonly faFlag = faFlag;
  readonly faScaleBalanced = faScaleBalanced;
  readonly faList = faList;
  readonly faCogs = faCogs;
  readonly faLock = faLock;
  readonly faUsersCog = faUsersCog;
  readonly faCloud = faCloud;
  readonly faDatabase = faDatabase;
  readonly faAsterisk = faAsterisk;
  readonly faSignInAlt = faSignInAlt;
  readonly faSignOutAlt = faSignOutAlt;
  readonly faUserPlus = faUserPlus;

  private readonly loginService = inject(LoginService);
  private readonly profileService = inject(ProfileService);
  private readonly router = inject(Router);

  constructor() {
    const { VERSION } = environment;
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    } else {
      this.version = '';
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction.set(profileInfo.inProduction ?? true);
      this.openAPIEnabled.set(profileInfo.openAPIEnabled ?? false);
    });
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed.set(true);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
