import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-overlay',
  standalone: true, 
  imports: [],
  templateUrl: './overlay.component.html',
  styleUrls: ['./overlay.component.css'],
})
export class OverlayComponent {
  @Input() isVisible = false;
  @Output() click = new EventEmitter<void>();

  onOverlayClick() {
    this.click.emit();
  }
}
