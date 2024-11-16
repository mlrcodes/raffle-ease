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
  styleUrl: './creation-form.component.css'
})
export class CreationFormComponent {
  constructor(
    private fb: FormBuilder
  ) {}

  @Output() createRaffle: EventEmitter<RaffleCreationRequest> = new EventEmitter<RaffleCreationRequest>();
  raffleForm!: FormGroup;

  initializeForm() {
    this.raffleForm = this.fb.group({
      title: ['', Validators.required], 
      description: ['', Validators.required], 
      endDate: ['', Validators.required],
      endTime: ['', Validators.required],
      imageKeys: this.fb.array([], Validators.required),
      amount: ['', Validators.required],
      price: ['', Validators.required],
      lowerLimit: ['', Validators.required],
      upperLimit: ['', Validators.required],
      associationId: 1
    })
  }

  get imageKeys(): FormArray {
    return this.raffleForm.get('imageKeys') as FormArray;
  }

  setImageKeys(imagesKeys: string[], markAsDirty = true) {
    this.imageKeys.clear();

    imagesKeys.forEach((image: string) => {
      this.imageKeys.push(this.fb.control(image, Validators.required));
    });
  }
  
  onSubmit(event: Event) {
    event.preventDefault();

    if (this.raffleForm.invalid) return;
        
    const { title, description, endDate, endTime, imageKeys, amount, price, lowerLimit, upperLimit, associationId } = this.raffleForm.value;
    
    const ticketsInfo: RaffleTicketsCreationRequest = {
      amount,
      price,
      lowerLimit,
      upperLimit
    };

    const endDateTime: Date = new Date(`${endDate}T${endTime}:00`);

    const raffleCreationRequest: RaffleCreationRequest = {
      title,
      description,
      endDate: endDateTime,  
      imageKeys,
      ticketsInfo,
      associationId
    }

    console.log(raffleCreationRequest)

    this.createRaffle.emit(raffleCreationRequest);
  }

  ngOnInit() {
    this.initializeForm();
  }
}
