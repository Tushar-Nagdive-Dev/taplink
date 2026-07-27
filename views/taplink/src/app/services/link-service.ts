import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { ILink, ILinkRequest } from '../interfaces/link.interface';

@Injectable({
  providedIn: 'root'
})
export class LinkService {

  // --- DUMMY JSON DATABASE ---
  private mockLinks: ILink[] = [
    {
      id: 1,
      title: 'My Developer Portfolio',
      url: 'https://johndoe.dev',
      isActive: true,
      position: 0,
      shortCode: 'A7xB9',
      isFavorite: true,
      colorCode: '#FFFFFF',
      createdAt: new Date().toISOString()
    },
    {
      id: 2,
      title: 'Follow me on Twitter',
      url: 'https://twitter.com/johndoe',
      isActive: true,
      position: 1,
      shortCode: 'mK2pL',
      isFavorite: false,
      colorCode: '#1DA1F2',
      createdAt: new Date().toISOString()
    },
    {
      id: 3,
      title: 'Latest YouTube Video',
      url: 'https://youtube.com/watch?v=123',
      isActive: false,
      position: 2,
      shortCode: 'v9YqR',
      isFavorite: false,
      colorCode: '#FF0000',
      createdAt: new Date().toISOString()
    }
  ];

  // --- READ ---
  getAllLinks(): Observable<ILink[]> {
    // Returns a copy of the array with a 600ms fake loading delay
    return of([...this.mockLinks]).pipe(delay(600));
  }

  // --- CREATE ---
  createLink(request: ILinkRequest): Observable<ILink> {
    var newLink: ILink = {
      id: Date.now(), // Fake DB ID
      title: request.title,
      url: request.url,
      isActive: request.isActive,
      position: 0,
      shortCode: Math.random().toString(36).substring(2, 7), // Fake ShortCode
      isFavorite: false,
      colorCode: '#FFFFFF',
      createdAt: new Date().toISOString()
    };

    // Add to our dummy database
    this.mockLinks.unshift(newLink);

    return of(newLink).pipe(delay(400));
  }

  // --- UPDATE ---
  updateLink(id: number, request: ILinkRequest): Observable<ILink> {
    const index = this.mockLinks.findIndex(l => l.id === id);

    if (index !== -1) {
      // Update our dummy database
      this.mockLinks[index] = {
        ...this.mockLinks[index],
        title: request.title,
        url: request.url,
        isActive: request.isActive
      };
      return of(this.mockLinks[index]).pipe(delay(400));
    }

    throw new Error('Link not found in dummy database');
  }

  // --- DELETE ---
  deleteLink(id: number): Observable<void> {
    // Remove from dummy database
    this.mockLinks = this.mockLinks.filter(l => l.id !== id);
    return of(void 0).pipe(delay(400));
  }
}
