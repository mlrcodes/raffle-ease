import { Component, EventEmitter, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthRequest } from '../../../../../../../core/models/auth/login-request';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  @Output() authRequest = new EventEmitter<AuthRequest>();
  authForm!: FormGroup;
  formSubmitted = false;

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.initializeForm();
  }

  initializeForm() {
    this.authForm = this.fb.group({
      email: ['', [
        Validators.required,
        Validators.email
      ]],
      password: ['', [
        Validators.required,
      ]]
    });
  }

  get lf(): { [key: string]: AbstractControl } {
    return this.authForm.controls;
  }

  getErrorMessage(field: string): string {
    const control = this.authForm.get(field);

    if (control?.hasError('required')) return 'This field is required.';
    if (control?.hasError('email')) return 'Enter a valid email address.';
    return '';
  }

  onSubmit(event: Event) {
    event.preventDefault();
    this.formSubmitted = true;

    if (this.authForm.invalid) {
      console.error('Form invalid:', this.authForm.errors);
      return;
    }

    const { email, password } = this.authForm.value;

    this.authRequest.emit({
      email: email || '',
      password: password || ''
    });
  }
}
