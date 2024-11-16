export interface EditRaffle {
    title?: string, 
    description?: string,
    endDate?: Date,
    imageKeys?: string[],
    ticketPrice?: number,
    totalTickets?: number
}
