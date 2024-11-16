import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RegisterRequest } from '../../../../../../../core/models/auth/register-request';
import { passwordMatchValidator } from '../../../../../../../core/validators/passwordMatch.validator';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {
  constructor(
    private fb: FormBuilder
  ) { }

  @Output() registerRequest: EventEmitter<RegisterRequest> = new EventEmitter<RegisterRequest>();
  authForm!: FormGroup;
  formSubmitted = false;

  initializeForm() {
    this.authForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [
        Validators.required,
        Validators.email
      ]],
      phoneNumber: ['', [
        Validators.required,
        Validators.pattern(/^\+\d{1,3}\d{4,14}$/),
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(16),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()-_+=])[A-Za-z\d!@#$%^&*()-_+=]{8,}$/),
      ]],
      confirmPassword: ['', Validators.required],
      city: ['', [
        Validators.required,
        Validators.minLength(2),
        Validators.pattern(/^[a-zA-Z\s]+$/)
      ]],
      zipCode: ['', [
        Validators.required,
        Validators.pattern(/^\d{4,6}$/)
      ]]
    }, { validators: passwordMatchValidator });
  }

  getErrorMessage(field: string): string {
    const control = this.authForm.get(field);

    if (control?.hasError('required')) return 'This field is required.';
    if (control?.hasError('email')) return 'Enter a valid email address.';
    if (control?.hasError('pattern')) {
      if (field === 'phoneNumber') return 'Enter a valid phone number (e.g., +123456789).';
      if (field === 'password') return 'Password must contain uppercase, lowercase, number, and special character.';
      if (field === 'city') return 'City name can only contain letters and spaces.';
      if (field === 'zipCode') return 'Enter a valid zip code (4-6 digits).';
    }
    if (control?.hasError('minlength')) return `Minimum length is ${control.errors?.['minlength'].requiredLength} characters.`;
    if (control?.hasError('maxlength')) return `Maximum length is ${control.errors?.['maxlength'].requiredLength} characters.`;
    if (field === 'confirmPassword' && this.authForm.hasError('passwordMismatch')) return 'Passwords do not match.';

    return '';
  }

  onSubmit(event: Event) {
    event.preventDefault();

    if (this.authForm.invalid) return;
    
    const { name, email, phoneNumber, password, confirmPassword, city, zipCode } = this.authForm.value;

    this.registerRequest.emit({
      name: name || '',
      email: email || '',
      phoneNumber: phoneNumber || '',
      password: password || '',
      confirmPassword: confirmPassword || '',
      city: city || '',
      zipCode: zipCode || ''
    });
  }


  ngOnInit() {
    this.initializeForm();
  }
}

