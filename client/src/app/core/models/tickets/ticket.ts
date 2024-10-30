export interface Ticket {
    id: string;
    raffleId: number;
    ticketNumber: string;
    status: TicketStatus;
    reservationFlag: string;
    reservationTime: Date;  
    customerId: string;
}

export type TicketStatus = 'AVAILABLE' | 'RESERVERD' | 'SOLD';