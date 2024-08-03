import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class WebService {

    constructor(public http: HttpClient){

    }

    public getData<T>(url: string, options?: any){
        return this.http.get<T>(url, options ? options : undefined)
    }

    public postData(url: string, jsonBody: any){
        return this.http.post(url, jsonBody);
    }

    public postWithFormData<T>(url: string, formData: FormData, options?: any){
        return this.http.post(url, formData, options);
    }

}