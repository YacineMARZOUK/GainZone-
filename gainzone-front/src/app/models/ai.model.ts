export interface ExerciseDto {
  name: string;
  sets: string;
  reps: string;
}

export interface AITrainingPlanResponse {
  name: string;
  description: string;
  frequency: string;
  durationWeeks: number;
  exercises: ExerciseDto[];
}
