import {ValueOptionsInterface} from '../interfaces/value-options.interface';

export class HasValueUtils {

  private constructor() {}

  private static readonly DEFAULT_OPTIONS: Readonly<ValueOptionsInterface> = {
    trim: true,
    treatWhitespaceAsEmpty: true,
    treatStringNullAsEmpty: true,
    treatNaNAsEmpty: true,
    treatInfinityAsEmpty: false,
    treatEmptyArrayAsEmpty: true,
    treatEmptyObjectAsEmpty: true,
    treatEmptyMapAsEmpty: true,
    treatEmptySetAsEmpty: true
  }

  /**
   * Determines whether the supplied value contains meaningful data.
   *
   * Supported types:
   * - null / undefined
   * - string
   * - number
   * - boolean
   * - bigint
   * - symbol
   * - array
   * - object
   * - Map
   * - Set
   * - Date
   * - File
   * - Blob
   * - ArrayBuffer
   * - TypedArray
   * - Promise
   * - RegExp
   * - URL
   * - class instances
   *
   * @example
   * ValueUtils.hasValue(name)
   *
   * @example
   * ValueUtils.hasValue(name, {
   *     trim: false
   * })
   *
   * @param value Value to validate.
   * @param options Optional validation configuration.
   * @returns true if the supplied value is considered meaningful.
   */
  static hasValue(value: unknown, options?: ValueOptionsInterface): boolean {
    const config = { ...HasValueUtils.DEFAULT_OPTIONS, ...options };

    if(typeof value === 'string') {
      let text = config.trim ? value.trim() : value;

      if(config.treatWhitespaceAsEmpty && text.length === 0) {
        return false;
      }

      if (config.treatStringNullAsEmpty) {
        switch (text.toLowerCase()) {
          case 'null':
          case 'undefined':
          case 'n/a':
          case 'nan':
            return false;
        }
      }
      return true;
    }
    if (typeof value === 'number') {
      if (config.treatNaNAsEmpty && Number.isNaN(value)) {
        return false;
      }
      return !(config.treatInfinityAsEmpty && !Number.isFinite(value));
    }

    if (typeof value === 'boolean' || typeof value === 'bigint' || typeof value === 'symbol' || typeof value === 'function') {
      return true;
    }

    if (Array.isArray(value)) {
      return config.treatEmptyArrayAsEmpty
        ? value.length > 0
        : true;
    }

    if (value instanceof Map) {
      return config.treatEmptyMapAsEmpty
        ? value.size > 0
        : true;
    }

    if (value instanceof Set) {
      return config.treatEmptySetAsEmpty
        ? value.size > 0
        : true;
    }

    if (value instanceof Date) {
      return !Number.isNaN(value.getTime());
    }

    if (value instanceof Blob) {
      return value.size > 0;
    }

    if (value instanceof ArrayBuffer) {
      return value.byteLength > 0;
    }

    if (ArrayBuffer.isView(value)) {
      return value.byteLength > 0;
    }

    if (typeof value === 'object' && Object.getPrototypeOf(value) === Object.prototype) {
      // @ts-ignore
      return config.treatEmptyObjectAsEmpty ? Object.keys(value).length > 0 : true;
    }
    return true;
  }
}
