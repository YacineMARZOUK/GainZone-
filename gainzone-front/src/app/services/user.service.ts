import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserUpdateRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8081/api/users';
  private adminUrl = 'http://localhost:8081/api/admin/users';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.adminUrl);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  updateUser(id: number, request: UserUpdateRequest): Observable<User> {
    return this.http.put<User>(`${this.adminUrl}/${id}`, request);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.adminUrl}/${id}`);
  }
}
