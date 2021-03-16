export interface ErrorResponse {
  message: string;
  rejectedFields: FieldError;
}

export interface FieldError {
  name: string;
  message: string;
}
