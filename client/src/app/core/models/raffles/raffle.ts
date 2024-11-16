export interface Raffle {
    id: number;
    title: string;
    description: string;
    startDate: Date; 
    endDate: string;
    status: RaffleStatus;
    imageKeys: string[];
    ticketPrice: number;
    availableTickets: number;
    soldTickets: number;
    totalTickets: number;
    revenue: number;
    associationId: number;
}

export type RaffleStatus = 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'PAUSED';