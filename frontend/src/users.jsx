import { useQuery } from "@tanstack/react-query";

const Users = () => {
	const { data, isLoading, isError, error } = useQuery({
		queryKey: ["users"], // unique key
		queryFn: async () => {
			const res = await fetch("https://jsonplaceholder.typicode.com/users");
			return res.json();
		},
		staleTime: 1000 * 5,
	});

	if (isLoading) return <p>Loading...</p>;
	if (isError) return <p>Error: {error.message}</p>;

	return (
		<ul>
			{data.map((user) => (
				<li key={user.id}>{user.name}</li>
			))}
		</ul>
	);
};

export default Users;
