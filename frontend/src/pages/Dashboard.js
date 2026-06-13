import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

function Dashboard() {
  const [forms, setForms] = useState([]);
  const [error, setError] = useState('');

  // Form fields
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [purpose, setPurpose] = useState('');

  // Edit mode tracking
  const [editingId, setEditingId] = useState(null);

  const navigate = useNavigate();
  const username = localStorage.getItem('username');

  // Page load hote hi forms fetch karo
  useEffect(() => {
    fetchForms();
  }, []);

  const fetchForms = async () => {
    try {
      const response = await api.get('/forms');
      setForms(response.data);
    } catch (err) {
      setError('Failed to load forms');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    navigate('/login');
  };

  const clearForm = () => {
    setFullName('');
    setEmail('');
    setPhone('');
    setAddress('');
    setPurpose('');
    setEditingId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const formData = { fullName, email, phone, address, purpose };

    try {
      if (editingId) {
        // UPDATE
        await api.put(`/forms/${editingId}`, formData);
      } else {
        // CREATE
        await api.post('/forms', formData);
      }
      clearForm();
      fetchForms();
    } catch (err) {
      setError('Failed to save form');
    }
  };

  const handleEdit = (form) => {
    setFullName(form.fullName);
    setEmail(form.email);
    setPhone(form.phone);
    setAddress(form.address);
    setPurpose(form.purpose);
    setEditingId(form.id);
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this form?')) return;

    try {
      await api.delete(`/forms/${id}`);
      fetchForms();
    } catch (err) {
      setError('Failed to delete form');
    }
  };

  return (
    <div style={{ maxWidth: '700px', margin: '40px auto', padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>Welcome, {username}!</h2>
        <button onClick={handleLogout}>Logout</button>
      </div>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {/* FORM */}
      <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px', borderRadius: '8px' }}>
        <h3>{editingId ? 'Edit Application' : 'New Application Form'}</h3>

        <form onSubmit={handleSubmit}>
          <input
            type="text" placeholder="Full Name"
            value={fullName} onChange={(e) => setFullName(e.target.value)}
            style={{ width: '100%', padding: '8px', marginBottom: '8px' }} required
          />
          <input
            type="email" placeholder="Email"
            value={email} onChange={(e) => setEmail(e.target.value)}
            style={{ width: '100%', padding: '8px', marginBottom: '8px' }} required
          />
          <input
            type="text" placeholder="Phone"
            value={phone} onChange={(e) => setPhone(e.target.value)}
            style={{ width: '100%', padding: '8px', marginBottom: '8px' }} required
          />
          <input
            type="text" placeholder="Address"
            value={address} onChange={(e) => setAddress(e.target.value)}
            style={{ width: '100%', padding: '8px', marginBottom: '8px' }} required
          />
          <input
            type="text" placeholder="Purpose"
            value={purpose} onChange={(e) => setPurpose(e.target.value)}
            style={{ width: '100%', padding: '8px', marginBottom: '8px' }} required
          />

          <button type="submit" style={{ padding: '8px 20px' }}>
            {editingId ? 'Update' : 'Submit'}
          </button>
          {editingId && (
            <button type="button" onClick={clearForm} style={{ marginLeft: '10px', padding: '8px 20px' }}>
              Cancel
            </button>
          )}
        </form>
      </div>

      {/* FORMS LIST */}
      <h3>Your Applications ({forms.length})</h3>

      {forms.length === 0 && <p>No applications submitted yet.</p>}

      {forms.map((form) => (
        <div key={form.id} style={{ border: '1px solid #ddd', padding: '12px', marginBottom: '10px', borderRadius: '6px' }}>
          <p><strong>{form.fullName}</strong> — {form.status}</p>
          <p>Email: {form.email} | Phone: {form.phone}</p>
          <p>Address: {form.address}</p>
          <p>Purpose: {form.purpose}</p>
          <p style={{ fontSize: '12px', color: '#888' }}>Submitted: {form.submittedDate}</p>

          <button onClick={() => handleEdit(form)} style={{ marginRight: '8px' }}>Edit</button>
          <button onClick={() => handleDelete(form.id)}>Delete</button>
        </div>
      ))}
    </div>
  );
}

export default Dashboard;