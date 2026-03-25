import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AITrainingPlanResponse } from '../models/ai.model';

@Injectable({
  providedIn: 'root'
})
export class AiService {
  private apiUrl = 'http://localhost:8081/api/analysis';

  constructor(private http: HttpClient) { }

  generatePlan(): Observable<AITrainingPlanResponse> {
    // Le token JWT étant déjà intercepté par AuthInterceptor, aucune auth manuelle n'est requise.
    return this.http.post<AITrainingPlanResponse>(`${this.apiUrl}/generate`, {});
  }
}
