import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MemberProfileRequest, MemberProfileResponse } from '../models/member.model';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private apiUrl = 'http://localhost:8081/api/members';

  constructor(private http: HttpClient) { }

  getProfile(): Observable<MemberProfileResponse> {
    return this.http.get<MemberProfileResponse>(`${this.apiUrl}/profile`);
  }

  updateProfile(request: MemberProfileRequest): Observable<MemberProfileResponse> {
    return this.http.put<MemberProfileResponse>(`${this.apiUrl}/profile`, request);
  }
}
