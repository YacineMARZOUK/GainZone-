export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  name?: string;
  lastName?: string;
  phone?: string;
  role: 'MEMBER' | 'COACH';
}

export interface AuthResponse {
  token: string;
  username: string;
  email: string;
  role: string;
}
