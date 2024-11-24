import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { UploadImagesComponent } from '../../../../shared/upload-images/upload-images.component';
import { EditRaffle } from '../../../../../../../core/models/raffles/edit-raffle';
import { Raffle } from '../../../../../../../core/models/raffles/raffle';

@Component({
  selector: 'app-edition-form',
  standalone: true,
  imports: [ReactiveFormsModule, UploadImagesComponent],
  templateUrl: './edition-form.component.html',
  styleUrls: ['./edition-form.component.css']
})
export class EditionFormComponent {
  @Input() raffle!: Raffle;
  @Output() editRaffle: EventEmitter<EditRaffle> = new EventEmitter<EditRaffle>();
  raffleForm!: FormGroup;
  formSubmitted = false;
  private hasSentInitialKeys = false;

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.initializeForm();
    if (this.raffle) {
      this.fillForm(this.raffle);
    }
  }

  initializeForm() {
    this.raffleForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      endDate: ['', Validators.required],
      endTime: ['', Validators.required],
      imageKeys: this.fb.array([]),
      totalTickets: ['', Validators.required],
      ticketPrice: ['', Validators.required]
    });
  }

  get imageKeys(): FormArray {
    return this.raffleForm.get('imageKeys') as FormArray;
  }

  setImageKeys(imageKeys: string[], markAsDirty = true) {
    this.imageKeys.clear();
    imageKeys.forEach((image: string) => this.imageKeys.push(this.fb.control(image)));
    if (markAsDirty) this.imageKeys.markAsDirty();
  }

  fillForm(raffle: Raffle): void {
    const endDateTime = new Date(raffle.endDate);
    this.raffleForm.patchValue({
      title: raffle.title,
      description: raffle.description,
      endDate: endDateTime.toISOString().split('T')[0],
      endTime: endDateTime.toISOString().split('T')[1].substring(0, 5),
      totalTickets: raffle.totalTickets,
      ticketPrice: raffle.ticketPrice
    });
    if (raffle.imageKeys) {
      this.setImageKeys(raffle.imageKeys, false);
      this.hasSentInitialKeys = true;
    }
  }

  onSubmit(event: Event) {
    event.preventDefault();
    this.formSubmitted = true;

    if (this.raffleForm.invalid) return;

    const modifiedData: Partial<EditRaffle> = this.getModifiedFields();
    this.editRaffle.emit(modifiedData);
  }

  getModifiedFields(): Partial<EditRaffle> {
    const modifiedData: Partial<EditRaffle> = {};
    Object.keys(this.raffleForm.controls).forEach((key) => {
      const control = this.raffleForm.get(key);
      if (control?.dirty) {
        if (key === 'endDate' || key === 'endTime') {
          const endDate = this.raffleForm.get('endDate')?.value;
          const endTime = this.raffleForm.get('endTime')?.value;
          modifiedData.endDate = new Date(`${endDate}T${endTime}:00`);
        } else if (key === 'imageKeys') {
          modifiedData.imageKeys = this.imageKeys.value;
        } else {
          modifiedData[key as keyof EditRaffle] = control.value;
        }
      }
    });
    return modifiedData;
  }
}
