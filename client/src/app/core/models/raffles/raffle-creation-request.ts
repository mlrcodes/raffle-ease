import { RaffleTicketsCreationRequest } from '../tickets/raffle-tickets-creation-request';

export interface RaffleCreationRequest {
    title: string, 
    description: string,
    endDate: Date,
    photosURLs: string[],
    ticketsInfo: RaffleTicketsCreationRequest,
    associationId: number
}
