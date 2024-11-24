import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { ImagesPreviewComponent } from "./images-preview/images-preview.component";
import { S3Service } from '../../../../../core/services/s3/s3-service.service';
import { forkJoin, map, switchMap, Observable } from 'rxjs';
import { ShareUrlsService } from '../../../../../core/services/raffles/share-urls.service';

@Component({
  selector: 'app-upload-images',
  standalone: true,
  imports: [ImagesPreviewComponent],
  templateUrl: './upload-images.component.html',
  styleUrl: './upload-images.component.css'
})
export class UploadImagesComponent {
  constructor(
    private s3Service: S3Service,
    private shareUls: ShareUrlsService
  ) { }

  @Input() imageKeys!: string[];
  @Output() imageKeysChange: EventEmitter<string[]> = new EventEmitter<string[]>();
  files: File[] = [];
  urls: string[] = [];

  onFilesSelected(event: any) {
    this.files.push(...event.target.files);
    this.uploadImages();
  }

  uploadImages() {
    const imageNames: string[] = this.files.map(file => `${file.name}_${Date.now()}`); 
    this.s3Service.getUploadUrls(imageNames).pipe(
      switchMap((urls: string[]) => {
        const uploadTasks: Observable<string>[] = this.files.map((file: File, index: number) => {
          const key: string = imageNames[index];
          const uploadUrl: string = urls[index];
          return this.s3Service.upload(file, uploadUrl).pipe(
            map(() => {
              return key
            })
          );
        });
        this.files = [];
        return forkJoin(uploadTasks);
      })
    ).subscribe({
      next: (imageKeys: string[]) => {
        this.imageKeys.push(...imageKeys);
        this.imageKeysChange.emit(this.imageKeys);
      }
    });
  }

  onDelete(index: number) {
    this.s3Service.delete(this.imageKeys[index]).subscribe({
      next: () => {
        this.imageKeys.splice(index, 1);
        this.urls.splice(index, 1);
        this.imageKeysChange.emit(this.imageKeys);
      }
    })
  }

  swapItems(index: number, direction: number) {
    const newIndex = index + direction;
    if (newIndex >= 0 && newIndex < this.imageKeys.length) {
      [this.imageKeys[index], this.imageKeys[newIndex]] = [this.imageKeys[newIndex], this.imageKeys[index]];
      [this.urls[index], this.urls[newIndex]] = [this.urls[newIndex], this.urls[index]];
    }
  }

  onMoveUp(index: number) {
    this.swapItems(index, -1);
    this.imageKeysChange.emit(this.imageKeys);
  }

  onMoveDown(index: number) {
    this.swapItems(index, 1);
    this.imageKeysChange.emit(this.imageKeys);
  }

  setKeys(keys: string[]) {
    this.imageKeys = keys;
    this.getViewUrls(keys);
  }

  getViewUrls(keys: string[]) {
    this.s3Service.getViewUrls(keys).subscribe({
      next: (urls: string[]) => {
        this.urls = urls;
      }
    })
  }

  sendUrls() {
    this.shareUls.event.subscribe({
      next: () => {
        this.shareUls.updateUrls(this.urls);
      }
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['imageKeys']) {
      this.setKeys(changes['imageKeys'].currentValue);
    }
  }

  ngOnInit() {
    this.sendUrls();
  }
}
