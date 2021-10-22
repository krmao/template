import { useState } from 'react';
import { Table } from 'antd';
import Link from 'next/link';

const columns = [{
  title: 'Username',
  dataIndex: 'username',
  key: 'username',
  render: (text) => (
    <Link href={`/user/[username]`} as={`/user/${text}`}>
      <a>{text}</a>
    </Link>
  )
}, {
  title: 'Email',
  dataIndex: 'email',
  key: 'email',
}];

const UserList = () => {
  const [list] = useState([{ username: 'admin', email: 'admin@xxx.com', key: '1' }]);
  return (
    <Table
      style={{ minWidth: '600px' }}
      dataSource={list}
      columns={columns}
      bordered
    />
  );
};

export default UserList;
