import {environment} from '../../environments/environment';

const BASE = environment.apiBaseUrl;

export const APIs = Object.freeze({
  AUTH : {
    LOGIN : `${BASE}/api/v1/auth/login`,
    LOGIN_SUCCESS : '/login-success',
    REGISTER : `${BASE}/api/v1/auth/register`,
    LOGOUT: `${BASE}/api/v1/auth/logout`,
    USER_SESSION: `${BASE}/api/v1/auth/session`,
  },
  USER : {
    USER_PROFILE : `${BASE}/api/v1/userprofile`,
  },
  USER_LINKS: {
    LINKS : `${BASE}/api/v1/links`,
    BY_ID :(id: number | string) => `${BASE}/api/v1/links/${id}`,
    FAVORITE_PATCHED :(id:number | string, isFavorite: boolean) => `${BASE}/api/v1/links/${id}/favorite?isFavorite=${isFavorite}`
  }
})
