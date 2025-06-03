import React from 'react';

interface OrderItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
  description: string;
}

const OrderPage: React.FC = () => {
  const orderItems: OrderItem[] = [
    { id: 1, name: 'Milk', price: 4.99, quantity: 1, description: 'White liquid' },
    { id: 2, name: 'Apple', price: 2.99, quantity: 10, description: 'Keeps docker away' },
    { id: 3, name: 'Gum', price: 0.99, quantity: 1, description: 'Chewy' },
  ];

  const calculateItemTotal = (price: number, quantity: number): number => {
    return price * quantity;
  };

  const calculateOrderTotal = (): number => {
    return orderItems.reduce((total, item) => total + calculateItemTotal(item.price, item.quantity), 0);
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl bg-gray-50">
      <h1 className="text-3xl font-bold mb-6 text-indigo-700 border-b-2 border-indigo-200 pb-2">Đơn hàng</h1>
      
      <div className="bg-white shadow-lg rounded-lg p-6 mb-8 border border-indigo-100">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6 bg-indigo-50 p-4 rounded-md">
          <div className="bg-white p-3 rounded shadow-sm">
            <p className="text-gray-600 text-sm">Họ và tên:</p>
            <p className="font-semibold text-indigo-800">NVA</p>
          </div>
          <div className="bg-white p-3 rounded shadow-sm">
            <p className="text-gray-600 text-sm">Ngày:</p>
            <p className="font-semibold text-indigo-800">9/11/2001</p>
          </div>
          <div className="bg-white p-3 rounded shadow-sm">
            <p className="text-gray-600 text-sm">Địa chỉ:</p>
            <p className="font-semibold text-indigo-800">xxx</p>
          </div>
        </div>
        
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white border border-indigo-100 rounded-lg overflow-hidden">
            <thead>
              <tr className="bg-indigo-600 text-white uppercase text-sm leading-normal">
                <th className="py-3 px-4 text-left">ID</th>
                <th className="py-3 px-4 text-left">Tên sản phẩm</th>
                <th className="py-3 px-4 text-right">Đơn giá</th>
                <th className="py-3 px-4 text-right">Số lượng</th>
                <th className="py-3 px-4 text-right">Thành tiền</th>
                <th className="py-3 px-4 text-left">Mô tả</th>
              </tr>
            </thead>
            <tbody className="text-gray-700 text-sm">
              {orderItems.map((item, index) => (
                <tr key={item.id} className={`border-b border-gray-200 hover:bg-indigo-50 transition duration-150 ${index % 2 === 0 ? 'bg-gray-50' : 'bg-white'}`}>
                  <td className="py-3 px-4 text-left">{item.id}</td>
                  <td className="py-3 px-4 text-left font-medium text-indigo-700">{item.name}</td>
                  <td className="py-3 px-4 text-right text-gray-600">${item.price.toFixed(2)}</td>
                  <td className="py-3 px-4 text-right text-gray-600">{item.quantity}</td>
                  <td className="py-3 px-4 text-right font-medium text-green-600">
                    ${calculateItemTotal(item.price, item.quantity).toFixed(2)}
                  </td>
                  <td className="py-3 px-4 text-left text-gray-500 italic">{item.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        
        <div className="mt-6 flex justify-end">
          <div className="bg-indigo-100 p-4 rounded-lg shadow-sm">
            <p className="text-lg font-bold text-indigo-800">
              Tổng cộng: <span className="text-green-600">${calculateOrderTotal().toFixed(2)}</span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderPage;