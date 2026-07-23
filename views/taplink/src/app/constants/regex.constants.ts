export const REGEX_CONTRACT = Object.freeze({
  USERNAME_PATTERN: '^[a-zA-Z0-9_-]+$',
  EMAIL_PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  MOBILE: /^[6-9]\d{9}$/,
  PASSWORD: /^(?=.*[A-Z])(?=.*\d).{8,}$/
})
