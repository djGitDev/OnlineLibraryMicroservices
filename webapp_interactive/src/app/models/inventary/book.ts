// export interface Book {
//   id: number;
//   isbn: string;
//   title: string;
//   description: string;
//   parutionDate: string;
//   price: number;
//   quantity: number;
//   publisherId: number;
// }


export interface Book {
  id: number;
  isbn: string;
  title: string;
  description: string;
  parutionDate: string;
  price: number;
  quantity: number;
  publisherId: number;
  publisherName: string;
  categoryNames: string[];
  authorNames: string[];
}
