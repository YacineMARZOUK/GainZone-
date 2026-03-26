import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TrainingProgram {
  id: number;
  name: string;
  description: string;
  frequency: string;
  durationWeeks: number;
  coachId: number;
  coachName: string;
  memberId: number;
  exercisesJson: string;
}

@Injectable({
  providedIn: 'root'
})
export class TrainingProgramService {
  private apiUrl = 'http://localhost:8081/api/training-programs';
  private usersUrl = 'http://localhost:8081/api/users';

  constructor(private http: HttpClient) { }

  getAll(): Observable<TrainingProgram[]> {
    return this.http.get<TrainingProgram[]>(this.apiUrl);
  }

  getById(id: number): Observable<TrainingProgram> {
    return this.http.get<TrainingProgram>(`${this.apiUrl}/${id}`);
  }

  getByMemberId(memberId: number): Observable<TrainingProgram[]> {
    return this.http.get<TrainingProgram[]>(`${this.apiUrl}/member/${memberId}`);
  }

  create(program: any): Observable<TrainingProgram> {
    return this.http.post<TrainingProgram>(this.apiUrl, program);
  }

  update(id: number, program: any): Observable<TrainingProgram> {
    return this.http.put<TrainingProgram>(`${this.apiUrl}/${id}`, program);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getMembers(): Observable<any[]> {
    return this.http.get<any[]>(this.usersUrl);
  }
}
