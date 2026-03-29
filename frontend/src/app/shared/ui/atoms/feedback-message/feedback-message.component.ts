import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-feedback-message',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feedback-message.component.html',
  styleUrls: ['./feedback-message.component.scss']
})
export class FeedbackMessageComponent {
  @Input() message = '';
  @Input() type: 'success' | 'error' = 'error';
}