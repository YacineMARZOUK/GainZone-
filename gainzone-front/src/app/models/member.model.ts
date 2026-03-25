export interface MemberProfileRequest {
  age: number | null;
  gender: string | null;
  weight: number | null;
  height: number | null;
  goal: string | null;
  fitnessLevel: string | null;
  profileImageUrl: string | null;
}

export interface MemberProfileResponse {
  id: number;
  age: number;
  gender: string;
  weight: number;
  height: number;
  goal: string;
  fitnessLevel: string;
  profileImageUrl: string;
}
