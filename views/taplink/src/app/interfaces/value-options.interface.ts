/**
 * Configuration options for ValueUtils.
 */
export interface ValueOptionsInterface {
  /**
   * Trims string values before validation.
   *
   * Default: true
   */
  trim?: boolean;

  /**
   * Treats whitespace-only strings as empty.
   *
   * Default: true
   */
  treatWhitespaceAsEmpty?: boolean;

  /**
   * Treats "null", "undefined", "n/a", and "nan"
   * string values as empty.
   *
   * Default: true
   */
  treatStringNullAsEmpty?: boolean;

  /**
   * Treats NaN as empty.
   *
   * Default: true
   */
  treatNaNAsEmpty?: boolean;

  /**
   * Treats Infinity and -Infinity as empty.
   *
   * Default: false
   */
  treatInfinityAsEmpty?: boolean;

  /**
   * Treats [] as empty.
   *
   * Default: true
   */
  treatEmptyArrayAsEmpty?: boolean;

  /**
   * Treats {} as empty.
   *
   * Default: true
   */
  treatEmptyObjectAsEmpty?: boolean;

  /**
   * Treats an empty Map as empty.
   *
   * Default: true
   */
  treatEmptyMapAsEmpty?: boolean;

  /**
   * Treats an empty Set as empty.
   *
   * Default: true
   */
  treatEmptySetAsEmpty?: boolean;
}
