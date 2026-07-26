import { Pipe, PipeTransform } from '@angular/core';
import {ValueOptionsInterface} from '../interfaces/value-options.interface';
import {HasValueUtils} from '../utils/has-value.utils';

@Pipe({
  name: 'hasValue',
  standalone: true,
  pure: true
})
export class HasValuePipe implements PipeTransform {

  transform(value: unknown, options?: ValueOptionsInterface): boolean {
    return HasValueUtils.hasValue(value, options);
  }

}
