import { useEffect, useState } from "react";
import api from "../api/axios";

export default function Home() {
  const [habits, setHabits] = useState([]);
  const [form, setForm] = useState({ name: "", description: "" });

  const fetchHabits = async () => {
    const res = await api.get("/habits");
    setHabits(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/habits", form);
    } catch (error) {
      alert(Object.values(error.response.data).join("\n"));
    }
    setForm({ name: "", description: "" });
    fetchHabits();
  };

  const handleDelete = async (id) => {
    await api.delete(`/habits/${id}`);
    fetchHabits();
  };

  useEffect(() => {
    fetchHabits();
  }, []);

  return (
    <div className="max-w-xl mx-auto mt-10 space-y-8">
      <h2 className="text-2xl font-bold">My Habits</h2>

      <form onSubmit={handleSubmit} className="space-y-2">
        <input
          className="w-full p-2 border rounded"
          placeholder="Habit name"
          value={form.name}
          onChange={(e) => setForm({ ...form, name: e.target.value })}
        />
        <input
          className="w-full p-2 border rounded"
          placeholder="Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
        />
        <button className="bg-blue-600 text-white px-4 py-2 rounded">
          Add Habit
        </button>
      </form>

      <ul className="space-y-2">
        {habits.map((habit) => (
          <li
            key={habit.id}
            className="border p-4 rounded flex justify-between"
          >
            <div>
              <h4 className="font-semibold">{habit.name}</h4>
              <p className="text-sm text-gray-600">{habit.description}</p>
            </div>
            <button
              onClick={() => handleDelete(habit.id)}
              className="text-red-500 hover:underline"
            >
              Delete
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
