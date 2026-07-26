import {environment} from '../../environments/environment';

const BASE = environment.apiBaseUrl;

export const APIs = Object.freeze({
  AUTH : {
    LOGIN : `${BASE}/api/v1/auth/login`,
    LOGIN_SUCCESS : '/login-success',
    REGISTER : `${BASE}/api/v1/auth/register`,
    LOGOUT: `${BASE}/api/v1/auth/logout`
  },
  USER : {
    GET_USER_PROFILE : `${BASE}/api/v1/userprofile`,
  }
})
