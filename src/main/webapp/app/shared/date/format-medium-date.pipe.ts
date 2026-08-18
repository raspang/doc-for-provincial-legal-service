import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';
import utc from 'dayjs/esm/plugin/utc';

dayjs.extend(utc);

@Pipe({
  name: 'formatMediumDate',
})
export default class FormatMediumDatePipe implements PipeTransform {
  transform(value: dayjs.Dayjs | string | null | undefined, forceUtc: boolean = false): string {
    if (!value) return '';

    let date = typeof value === 'string' ? dayjs(value) : value;

    if (!date.isValid()) {
      return 'Invalid Date';
    }

    if (forceUtc) {
      date = date.utc();
    }

    return date.format('D MMM YYYY');
  }
}
