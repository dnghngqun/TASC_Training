import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class EncryptionService {
  constructor() {}

  encode(data: string): string {
    return btoa(data);
  }
  decode(encodedData: string): string {
    return atob(encodedData);
  }

  decodeObject(encodedData: string): Object {
    return JSON.parse(decodeURIComponent(escape(atob(encodedData))));
  }
}
