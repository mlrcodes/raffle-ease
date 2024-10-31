import { Component, EventEmitter, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RaffleTicketsCreationRequest } from '../../../../../core/models/tickets/raffle-tickets-creation-request';
import { RaffleCreationRequest } from '../../../../../core/models/raffles/raffle-creation-request';

@Component({
  selector: 'app-raffle-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './raffle-form.component.html',
  styleUrl: './raffle-form.component.css'
})
export class RaffleFormComponent {

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
      photosURLs: this.fb.array([], Validators.required),
      amount: ['', Validators.required],
      price: ['', Validators.required],
      lowerLimit: ['', Validators.required],
      upperLimit: ['', Validators.required],
      associationId: 1
    })
  }

  get photosURLs(): FormArray {
    return this.raffleForm.get('photosURLs') as FormArray;
  }
  
  addPhotoURL(url: string) {
    this.photosURLs.push(this.fb.control(url, Validators.required));
  }

  removePhotoURL(index: number) {
    this.photosURLs.removeAt(index);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    
    if (input.files) {
      Array.from(input.files).forEach((file: File) => {
        const url: string = URL.createObjectURL(file);
        this.addPhotoURL(url);
      });
    }
  }

  onSubmit(event: Event) {
    event.preventDefault();

    if (this.raffleForm.invalid) return;
        
    const { title, description, endDate, endTime, photosURLs, amount, price, lowerLimit, upperLimit, associationId } = this.raffleForm.value;
    
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
      photosURLs,
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
