/**
 * Represents the payload sent to Spring Boot to create or update a link.
 * Matches: LinkRequest.java
 */
export interface ILinkRequest {
  title: string;
  url: string;
  isActive: boolean;
}

/**
 * Represents the comprehensive Link object returned by Spring Boot.
 * This combines data from UserLinks, LinkRouting, and LinkPresentation.
 * Matches: LinkResponse.java (Make sure your Java DTO returns all these fields!)
 */
export interface ILink {
  // Core (UserLinks)
  id: number;
  title: string;
  url: string;
  position: number;
  isActive: boolean;
  createdAt: string;

  // Routing (LinkRouting)
  shortCode: string;
  customSlug?: string; // Optional, for future custom URLs
  expiresAt?: string;  // Optional, if you want links to expire

  // Presentation (LinkPresentation)
  isFavorite: boolean;
  colorCode: string;
  label?: string;      // The internal label, if different from public title

  // Relations (LinkTags) - Ready for when we build the tagging UI!
  tags?: ITag[];
}

/**
 * Represents a user-created category/tag.
 * Matches: Tags.java
 */
export interface ITag {
  id: number;
  name: string;
  badgeColor: string;
}
