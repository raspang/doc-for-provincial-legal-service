import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';
import utc from 'dayjs/esm/plugin/utc';

// Extend dayjs with UTC support to prevent timezone shifting
dayjs.extend(utc);

@Pipe({
  name: 'formatMediumDatetime',
})
export default class FormatMediumDatetimePipe implements PipeTransform {
  // Accept both Dayjs objects and raw Strings
  transform(value: dayjs.Dayjs | string | null | undefined, forceUtc: boolean = false): string {
    if (!value) return '';

    // Parse if it's a raw string from the API
    let date = typeof value === 'string' ? dayjs(value) : value;

    if (!date.isValid()) {
      return 'Invalid Date';
    }

    // If forceUtc is true, it prevents the browser from converting to local time
    if (forceUtc) {
      date = date.utc();
    }

    return date.format('MMM[.] D, YYYY h:mma');
  }
}
