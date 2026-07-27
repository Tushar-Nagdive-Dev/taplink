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

export interface ISessionResponse {
  authenticated: boolean;
  userId: string;
  username: string;
  email: string;
  fistName: string;
  lastName: string;
  roles: string[];
}
