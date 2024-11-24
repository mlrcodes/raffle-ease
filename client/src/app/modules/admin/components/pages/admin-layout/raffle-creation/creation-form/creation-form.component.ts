import { Component, EventEmitter, Output } from '@angular/core';
import { Form, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RaffleCreationRequest } from '../../../../../../../core/models/raffles/raffle-creation-request';
import { UploadImagesComponent } from '../../../../shared/upload-images/upload-images.component';
import { RaffleTicketsCreationRequest } from '../../../../../../../core/models/tickets/raffle-tickets-creation-request';

@Component({
  selector: 'app-creation-form',
  standalone: true,
  imports: [ReactiveFormsModule, UploadImagesComponent],
  templateUrl: './creation-form.component.html',
  styleUrls: ['./creation-form.component.css']
})
export class CreationFormComponent {
  @Output() createRaffle: EventEmitter<RaffleCreationRequest> = new EventEmitter<RaffleCreationRequest>();
  raffleForm!: FormGroup;
  formSubmitted = false;

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.initializeForm();
  }

  initializeForm() {
    this.raffleForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      endDate: ['', Validators.required],
      endTime: ['', Validators.required],
      imageKeys: this.fb.array([], Validators.required),
      amount: ['', Validators.required],
      price: ['', Validators.required],
      lowerLimit: ['', Validators.min(0)],
      associationId: [1, Validators.required]
    });
  }

  get imageKeys(): FormArray {
    return this.raffleForm.get('imageKeys') as FormArray;
  }

  setImageKeys(imagesKeys: string[], markAsDirty = true) {
    this.imageKeys.clear();
    imagesKeys.forEach((image: string) => this.imageKeys.push(this.fb.control(image)));
    if (markAsDirty) this.imageKeys.markAsDirty();
  }

  onSubmit(event: Event) {
    event.preventDefault();
    this.formSubmitted = true;

    if (this.raffleForm.invalid) return;

    const { title, description, endDate, endTime, imageKeys, amount, price, lowerLimit, upperLimit, associationId } = this.raffleForm.value;

    const ticketsInfo: RaffleTicketsCreationRequest = {
      amount,
      price,
      lowerLimit
    };

    const endDateTime: Date = new Date(`${endDate}T${endTime}:00`);

    const raffleCreationRequest: RaffleCreationRequest = {
      title,
      description,
      endDate: endDateTime,
      imageKeys,
      ticketsInfo
    };

    this.createRaffle.emit(raffleCreationRequest);
  }
}
