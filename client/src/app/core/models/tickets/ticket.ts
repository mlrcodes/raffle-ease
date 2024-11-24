export interface Ticket {
    id: string;
    raffleId: number;
    ticketNumber: string;
    price: number;
    status: TicketStatus;
    reservationFlag: string;
    reservationTime: Date;  
    customerId: string;
}

export type TicketStatus = 'AVAILABLE' | 'RESERVERD' | 'SOLD';