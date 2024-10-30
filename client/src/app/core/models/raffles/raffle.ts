export interface Raffle {
    id: number;
    title: string;
    description: string;
    startDate: Date; 
    endDate: string;
    status: RaffleStatus;
    photosURLs: Set<string>;
    ticketPrice: number;
    availableTickets: number;
    totalTickets: number;
    associationId: number;
}

export type RaffleStatus = 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'CANCELED';