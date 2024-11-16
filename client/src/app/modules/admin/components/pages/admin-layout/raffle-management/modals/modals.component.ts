import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-modals',
  standalone: true,
  imports: [NgClass],
  templateUrl: './modals.component.html',
  styleUrl: './modals.component.css'
})
export class ModalsComponent {
  @Input() action!: 'publish' | 'pause' | 'restart' | 'delete' | null;
  @Input() raffleId!: number;
  @Input() display!: boolean;
  @Output() confirmed: EventEmitter<void> = new EventEmitter<void>();
  @Output() closed: EventEmitter<void> = new EventEmitter<void>();
  header!: string;
  body!: string;

  confirm() {
    this.confirmed.emit();
  }

  close() {
    this.closed.emit();
  }

  private setModalTexts() {
    switch (this.action) {
      case 'publish':
        this.header = 'Confirm Publish';
        this.body = 'Are you sure you want to publish?';
        break;
      case 'pause':
        this.header = 'Confirm Pause';
        this.body = 'Are you sure you want to pause?';
        break;
      case 'restart':
        this.header = 'Confirm Restart';
        this.body = 'Are you sure you want to restart?';
        break;
      case 'delete':
        this.header = 'Confirm Delete';
        this.body = 'Are you sure you want to delete?';
        break;
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['action']) this.setModalTexts();
  }
}
