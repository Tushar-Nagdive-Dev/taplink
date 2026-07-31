export const AppConstants = Object.freeze({
  TOAST_TYPE: {
    SUCCESS: 'success',
    ERROR: 'error',
    INFO: 'info',
    WARNING: 'warning',
  },
  TOAST_MESSAGES: {
    FILL_TITLE_URL : 'Please fill in both title and URL',
    TOAST_USER_PROFILE_SAVE_MSG: 'User profile updated successfully.',
    TOAST_USER_PROFILE_SAVE_FAILED: 'User profile update failed',
    LINK_ADDED_SUCCESSFULLY : 'Link added successfully!',
    FAILED_TO_CREATE_LINK : 'Failed to create link',
    FAILED_TO_LOAD_LINKS : 'Failed to load links',
    COPIED_TO_CLIPBOARD : 'Copied to clipboard!',
    FAILED_TO_DELETE_LINK : 'Failed to delete link',
    ARE_SURE_YOU_WANT_T0_DELETE_LINK : 'Are you sure you want to delete this link?',
    LINK_DELETED : 'Link deleted',
    FAILED_TO_SAVE_CHANGES : 'Failed to save changes',
    FAILED_TO_UPDATE_FAVORITE_STATUS :'Failed to update favorite status',
    FAILED_TO_SAVE_STATUS : 'Failed to save status'
  },
  AUTH_KEY : 'taplink_is_auth',
  SOMETHING_WENT_WRONG : 'Something went wrong while verifying your credentials.',
  SESSION_EXPIRED : 'Your session has expired. Please log in again to continue.',
  CHECK_USERNAME_AND_PASSWORD : 'We could not log you in. Please check your username and password.',
  USERNAME_ALREADY_TAKEN : 'There was a problem creating your account. That username might be taken.',
  LOGIN_SUCCESSFUL : 'Login successful! Welcome back.',
  TIMEZONE: 'timeZone',
  CHANGE_DISPLAY_NAME : 'Change Display Name?',
  CHANGE_DISPLAY_NAME_MSG : 'You are about to change your public display name from',
  TO: 'to',
  FAILED_LOAD_LINKS : 'Failed to load links'
});
