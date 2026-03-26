export interface User {
  id: number;
  username: string;
  email: string;
  name: string;
  lastName: string;
  role: 'MEMBER' | 'COACH' | 'ADMIN';
  phone: string;
}

export interface UserUpdateRequest {
  username?: string;
  email?: string;
  password?: string;
  name?: string;
  lastName?: string;
  phone?: string;
  role?: string;
}
