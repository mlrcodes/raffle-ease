import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class S3Service {

  constructor(
    private httpClient: HttpClient
  ) { }

  private baseURL: string = 'http://localhost:8080/api/v1/s3';

  upload(file: File, uploadUrl: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': file.type });
    return this.httpClient.put(uploadUrl, file, { headers, responseType: 'text' }).pipe(switchMap(() => of(undefined)));
  }

  getViewUrls(keys: string[]): Observable<string[]> {
    return this.httpClient.post(`${this.baseURL}/get-view-urls`, { fileNames: keys }) as Observable<string[]>;
  }
  
  getUploadUrls(imageNames: string[]): Observable<string[]> {
    return this.httpClient.post<string[]>(`${this.baseURL}/get-upload-urls`, { fileNames: imageNames});
  } 

  delete(fileName: string): Observable<void> {
    const params = new HttpParams().set('file', fileName);
    return this.httpClient.delete<void>(`${this.baseURL}/delete`, { params });
  }
}
