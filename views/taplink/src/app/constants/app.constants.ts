import {ToastType} from '../modals/app.modal';

export const AppConstants = Object.freeze({
  TOAST_TYPE: {
    SUCCESS: 'success',
    ERROR: 'error',
    INFO: 'info',
    WARNING: 'warning',
  },
  AUTH_KEY : 'taplink_is_auth',
  SOMETHING_WENT_WRONG : 'Something went wrong while verifying your credentials.',
  SESSION_EXPIRED : 'Your session has expired. Please log in again to continue.',
  CHECK_USERNAME_AND_PASSWORD : 'We could not log you in. Please check your username and password.',
  USERNAME_ALREADY_TAKEN : 'There was a problem creating your account. That username might be taken.',
  LOGIN_SUCCESSFUL : 'Login successful! Welcome back.',
  TIMEZONE: 'timeZone'
});
