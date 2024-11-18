import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataCartService {
  private sharedData: any;

  setData(data: any) {
    this.sharedData = data;
  }

  getData() {
    return this.sharedData;
  }
}
