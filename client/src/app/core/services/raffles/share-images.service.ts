import { Injectable } from "@angular/core";
import { BehaviorSubject, filter, Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ShareImagesService {

    constructor() { }

    private images: Map<number, string[]> = new Map<number, string[]>();
    private imagesSubject: BehaviorSubject<Map<number, string[]>> = new BehaviorSubject<Map<number, string[]>>(this.images);
    imagesUpdates: Observable<Map<number, string[]>> = this.imagesSubject.asObservable().pipe(
        filter(value => !!value)
    );

    updateImages(images: Map<number, string[]>) {
        this.images = images;
        this.imagesSubject.next(images);
    }

    setImages(raffleId: number, imageKeys: string[]) {
        this.images.set(raffleId, imageKeys);
        this.imagesSubject.next(this.images);
    }

    get(id: number): string[] | undefined {
        return this.images.get(id);
    }

    getAll() {
        return this.images;
    }


    delete(id: number) {
        this.images.delete(id);
    }

    isNull() {
        return !this.images;
    }
}