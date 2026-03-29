import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FeedbackMessageComponent } from '../../atoms/feedback-message/feedback-message.component';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FeedbackMessageComponent
  ],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
  @Input({ required: true }) formGroup!: FormGroup;
  @Input() isLoading = false;
  @Input() successMessage = '';
  @Input() errorMessage = '';
  @Input() hidePassword = true;

  @Output() submitForm = new EventEmitter<void>();
  @Output() ssoClick = new EventEmitter<void>();

  onSubmit(): void {
    this.submitForm.emit();
  }

  onSsoClick(): void {
    this.ssoClick.emit();
  }
}