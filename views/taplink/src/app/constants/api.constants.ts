import {environment} from '../../environments/environment';

const BASE = environment.apiBaseUrl;
const APIs_v1 = `${BASE}/api/v1`;

export const APIs = Object.freeze({
  AUTH : {
    LOGIN : `${APIs_v1}/auth/login`,
    LOGIN_SUCCESS : '/login-success',
    REGISTER : `${APIs_v1}/auth/register`,
    LOGOUT: `${APIs_v1}/auth/logout`,
    USER_SESSION: `${APIs_v1}/auth/session`,
  },
  USER : {
    USER_PROFILE : `${APIs_v1}/userprofile`,
  },
  USER_LINKS: {
    LINKS : `${APIs_v1}/links`,
    BY_ID :(id: number | string) => `${APIs_v1}/links/${id}`,
    FAVORITE_PATCHED :(id:number | string, isFavorite: boolean) => `${APIs_v1}/links/${id}/favorite?isFavorite=${isFavorite}`,
    UPDATE_STATUS : (id: number | string, isActive: boolean) => `${APIs_v1}/links/${id}/status?isActive=${isActive}`,
    LINKS_REORDER : `${APIs_v1}/links/reorder`,
    GET_QR_CODE : (id: number | string)=> `${APIs_v1}/qrcode/${id}/qr`,
  }
})
