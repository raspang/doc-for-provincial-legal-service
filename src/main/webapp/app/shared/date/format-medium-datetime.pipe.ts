import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';
import utc from 'dayjs/esm/plugin/utc';

dayjs.extend(utc);

@Pipe({
  name: 'formatMediumDatetime',
})
export default class FormatMediumDatetimePipe implements PipeTransform {
  //  Removed ": boolean" here
  transform(value: dayjs.Dayjs | string | null | undefined, forceUtc = false): string {
    if (!value) return '';

    let date = typeof value === 'string' ? dayjs(value) : value;

    if (!date.isValid()) {
      return 'Invalid Date';
    }

    if (forceUtc) {
      date = date.utc();
    }

    return date.format('MMM[.] D, YYYY h:mma');
  }
}
