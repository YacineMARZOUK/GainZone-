export interface Activity {
  id: number;
  name: string;
  description: string;
  type: string;
  dateTime: string;
  durationMinutes: number;
  maxParticipants: number;
  currentParticipantsCount: number;
  coachId: number;
  coachName: string;
}
