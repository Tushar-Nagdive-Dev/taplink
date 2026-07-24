export interface IRegisterRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
}

export interface ILoginRequest {
  username: string;
  password: string;
}

export interface IAuthResponse {
  authMessage: string;
}
