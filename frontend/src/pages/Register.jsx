import { useState } from "react";
import api from "../api/axios";

export default function Register() {
  const [form, setForm] = useState({ username: "", password: "" });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post("/auth/register", form);
    alert("Registered. Now login.");
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-md mx-auto space-y-4">
      <input
        className="w-full p-2 border rounded"
        placeholder="Username"
        onChange={(e) => setForm({ ...form, username: e.target.value })}
      />
      <input
        className="w-full p-2 border rounded"
        type="password"
        placeholder="Password"
        onChange={(e) => setForm({ ...form, password: e.target.value })}
      />
      <button className="bg-blue-600 text-white px-4 py-2 rounded">
        Register
      </button>
    </form>
  );
}
