import { Injectable } from "@angular/core";
import { encode } from "querystring";

@Injectable()
export class UtilService {

    getBlobFromByteArray(byteArrayBase64: string, mimeType: string){
        // Convert Base64 to byte array
        const byteCharacters = atob(byteArrayBase64);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);

        // Create a Blob from the byte array
        return new Blob([byteArray], { type: mimeType });
    }

    downloadImageFromByteArray(byteArrayBase64: string, mimeType: string){
        // Create a Blob from the byte array
        const blob = this.getBlobFromByteArray(byteArrayBase64, mimeType);

        // Create a URL for the Blob
        const url = URL.createObjectURL(blob);

        // Create an anchor element and set its href to the Blob URL
        const a = document.createElement('a');
        a.href = url;
        a.download = 'image.png'; // Set the file name

        // Append the anchor to the body
        document.body.appendChild(a);

        // Trigger a click event on the anchor
        a.click();

        // Remove the anchor from the body
        document.body.removeChild(a);

        // Revoke the Blob URL to free up resources
        URL.revokeObjectURL(url);
    }

    getDataUrlFromByteArray(byteArrayBase64: string, mimeType: string){
        // Convert Base64 to byte array
        const byteCharacters = atob(byteArrayBase64);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);

        return 'data:image/png;base64,' + byteArrayBase64;
        
    }
}